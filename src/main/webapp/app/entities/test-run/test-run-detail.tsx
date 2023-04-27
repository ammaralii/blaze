import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-run.reducer';

export const TestRunDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testRunEntity = useAppSelector(state => state.testRun.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testRunDetailsHeading">Test Run</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testRunEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{testRunEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{testRunEntity.description}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{testRunEntity.createdAt ? <TextFormat value={testRunEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{testRunEntity.createdBy}</dd>
          <dt>Test Level</dt>
          <dd>{testRunEntity.testLevel ? testRunEntity.testLevel.id : ''}</dd>
          <dt>Mile Stone</dt>
          <dd>{testRunEntity.mileStone ? testRunEntity.mileStone.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-run" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-run/${testRunEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestRunDetail;
