import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestRunDetails } from 'app/shared/model/test-run-details.model';
import { getEntities } from './test-run-details.reducer';

export const TestRunDetails = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const testRunDetailsList = useAppSelector(state => state.testRunDetails.entities);
  const loading = useAppSelector(state => state.testRunDetails.loading);
  const totalItems = useAppSelector(state => state.testRunDetails.totalItems);

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
      <h2 id="test-run-details-heading" data-cy="TestRunDetailsHeading">
        Test Run Details
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/test-run-details/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Test Run Details
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {testRunDetailsList && testRunDetailsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('resultDetail')}>
                  Result Detail <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('jiraId')}>
                  Jira Id <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  Created By <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('executedBy')}>
                  Executed By <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Test Run <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Test Case <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Status <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {testRunDetailsList.map((testRunDetails, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/test-run-details/${testRunDetails.id}`} color="link" size="sm">
                      {testRunDetails.id}
                    </Button>
                  </td>
                  <td>{testRunDetails.resultDetail}</td>
                  <td>{testRunDetails.jiraId}</td>
                  <td>{testRunDetails.createdBy}</td>
                  <td>{testRunDetails.executedBy}</td>
                  <td>
                    {testRunDetails.testRun ? <Link to={`/test-run/${testRunDetails.testRun.id}`}>{testRunDetails.testRun.id}</Link> : ''}
                  </td>
                  <td>
                    {testRunDetails.testCase ? (
                      <Link to={`/test-case/${testRunDetails.testCase.id}`}>{testRunDetails.testCase.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {testRunDetails.status ? <Link to={`/test-status/${testRunDetails.status.id}`}>{testRunDetails.status.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/test-run-details/${testRunDetails.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/test-run-details/${testRunDetails.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/test-run-details/${testRunDetails.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          !loading && <div className="alert alert-warning">No Test Run Details found</div>
        )}
      </div>
      {totalItems ? (
        <div className={testRunDetailsList && testRunDetailsList.length > 0 ? '' : 'd-none'}>
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

export default TestRunDetails;
