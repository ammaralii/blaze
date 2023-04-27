import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-run-details.reducer';

export const TestRunDetailsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testRunDetailsEntity = useAppSelector(state => state.testRunDetails.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testRunDetailsDetailsHeading">Test Run Details</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testRunDetailsEntity.id}</dd>
          <dt>
            <span id="resultDetail">Result Detail</span>
          </dt>
          <dd>{testRunDetailsEntity.resultDetail}</dd>
          <dt>
            <span id="jiraId">Jira Id</span>
          </dt>
          <dd>{testRunDetailsEntity.jiraId}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{testRunDetailsEntity.createdBy}</dd>
          <dt>
            <span id="executedBy">Executed By</span>
          </dt>
          <dd>{testRunDetailsEntity.executedBy}</dd>
          <dt>Test Run</dt>
          <dd>{testRunDetailsEntity.testRun ? testRunDetailsEntity.testRun.id : ''}</dd>
          <dt>Test Case</dt>
          <dd>{testRunDetailsEntity.testCase ? testRunDetailsEntity.testCase.id : ''}</dd>
          <dt>Status</dt>
          <dd>{testRunDetailsEntity.status ? testRunDetailsEntity.status.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-run-details" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-run-details/${testRunDetailsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestRunDetailsDetail;
