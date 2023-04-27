import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './role.reducer';

export const RoleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const roleEntity = useAppSelector(state => state.role.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="roleDetailsHeading">Role</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{roleEntity.id}</dd>
          <dt>
            <span id="roleName">Role Name</span>
          </dt>
          <dd>{roleEntity.roleName}</dd>
          <dt>
            <span id="isdefault">Isdefault</span>
          </dt>
          <dd>{roleEntity.isdefault ? 'true' : 'false'}</dd>
          <dt>Permission</dt>
          <dd>
            {roleEntity.permissions
              ? roleEntity.permissions.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {roleEntity.permissions && i === roleEntity.permissions.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/role" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/role/${roleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default RoleDetail;
