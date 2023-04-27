import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-suite.reducer';

export const TestSuiteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testSuiteEntity = useAppSelector(state => state.testSuite.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testSuiteDetailsHeading">Test Suite</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testSuiteEntity.id}</dd>
          <dt>
            <span id="testSuiteName">Test Suite Name</span>
          </dt>
          <dd>{testSuiteEntity.testSuiteName}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{testSuiteEntity.description}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{testSuiteEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>
            {testSuiteEntity.createdAt ? <TextFormat value={testSuiteEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedBy">Updated By</span>
          </dt>
          <dd>{testSuiteEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>
            {testSuiteEntity.updatedAt ? <TextFormat value={testSuiteEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>Project</dt>
          <dd>{testSuiteEntity.project ? testSuiteEntity.project.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-suite" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-suite/${testSuiteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestSuiteDetail;
