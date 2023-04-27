import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './application-user.reducer';

export const ApplicationUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const applicationUserEntity = useAppSelector(state => state.applicationUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="applicationUserDetailsHeading">Application User</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{applicationUserEntity.id}</dd>
          <dt>
            <span id="firstName">First Name</span>
          </dt>
          <dd>{applicationUserEntity.firstName}</dd>
          <dt>
            <span id="lastName">Last Name</span>
          </dt>
          <dd>{applicationUserEntity.lastName}</dd>
          <dt>
            <span id="password">Password</span>
          </dt>
          <dd>{applicationUserEntity.password}</dd>
          <dt>
            <span id="lastActive">Last Active</span>
          </dt>
          <dd>
            {applicationUserEntity.lastActive ? (
              <TextFormat value={applicationUserEntity.lastActive} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{applicationUserEntity.status}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{applicationUserEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>
            {applicationUserEntity.createdAt ? (
              <TextFormat value={applicationUserEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedBy">Updated By</span>
          </dt>
          <dd>{applicationUserEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>
            {applicationUserEntity.updatedAt ? (
              <TextFormat value={applicationUserEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="userEmail">User Email</span>
          </dt>
          <dd>{applicationUserEntity.userEmail}</dd>
          <dt>
            <span id="isDeleted">Is Deleted</span>
          </dt>
          <dd>{applicationUserEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>Company</dt>
          <dd>{applicationUserEntity.company ? applicationUserEntity.company.id : ''}</dd>
          <dt>Project</dt>
          <dd>
            {applicationUserEntity.projects
              ? applicationUserEntity.projects.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {applicationUserEntity.projects && i === applicationUserEntity.projects.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>Role</dt>
          <dd>
            {applicationUserEntity.roles
              ? applicationUserEntity.roles.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {applicationUserEntity.roles && i === applicationUserEntity.roles.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/application-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/application-user/${applicationUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ApplicationUserDetail;
