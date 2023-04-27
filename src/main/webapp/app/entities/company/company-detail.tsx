import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './company.reducer';

export const CompanyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const companyEntity = useAppSelector(state => state.company.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="companyDetailsHeading">Company</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{companyEntity.id}</dd>
          <dt>
            <span id="country">Country</span>
          </dt>
          <dd>{companyEntity.country}</dd>
          <dt>
            <span id="companyAddress">Company Address</span>
          </dt>
          <dd>{companyEntity.companyAddress}</dd>
          <dt>
            <span id="organization">Organization</span>
          </dt>
          <dd>{companyEntity.organization}</dd>
          <dt>
            <span id="expectedNoOfUsers">Expected No Of Users</span>
          </dt>
          <dd>{companyEntity.expectedNoOfUsers}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{companyEntity.createdAt ? <TextFormat value={companyEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/company" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/company/${companyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompanyDetail;
