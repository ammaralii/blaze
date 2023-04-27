import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IApplicationUser } from 'app/shared/model/application-user.model';
import { getEntities } from './application-user.reducer';

export const ApplicationUser = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const applicationUserList = useAppSelector(state => state.applicationUser.entities);
  const loading = useAppSelector(state => state.applicationUser.loading);
  const totalItems = useAppSelector(state => state.applicationUser.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="application-user-heading" data-cy="ApplicationUserHeading">
        Application Users
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/application-user/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Application User
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {applicationUserList && applicationUserList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('firstName')}>
                  First Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lastName')}>
                  Last Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('password')}>
                  Password <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lastActive')}>
                  Last Active <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  Created By <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  Created At <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedBy')}>
                  Updated By <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  Updated At <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('userEmail')}>
                  User Email <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  Is Deleted <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Company <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {applicationUserList.map((applicationUser, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/application-user/${applicationUser.id}`} color="link" size="sm">
                      {applicationUser.id}
                    </Button>
                  </td>
                  <td>{applicationUser.firstName}</td>
                  <td>{applicationUser.lastName}</td>
                  <td>{applicationUser.password}</td>
                  <td>
                    {applicationUser.lastActive ? (
                      <TextFormat type="date" value={applicationUser.lastActive} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{applicationUser.status}</td>
                  <td>{applicationUser.createdBy}</td>
                  <td>
                    {applicationUser.createdAt ? (
                      <TextFormat type="date" value={applicationUser.createdAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{applicationUser.updatedBy}</td>
                  <td>
                    {applicationUser.updatedAt ? (
                      <TextFormat type="date" value={applicationUser.updatedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{applicationUser.userEmail}</td>
                  <td>{applicationUser.isDeleted ? 'true' : 'false'}</td>
                  <td>
                    {applicationUser.company ? <Link to={`/company/${applicationUser.company.id}`}>{applicationUser.company.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/application-user/${applicationUser.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/application-user/${applicationUser.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/application-user/${applicationUser.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Application Users found</div>
        )}
      </div>
      {totalItems ? (
        <div className={applicationUserList && applicationUserList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default ApplicationUser;
