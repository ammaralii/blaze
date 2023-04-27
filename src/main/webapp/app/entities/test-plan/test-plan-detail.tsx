import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-plan.reducer';

export const TestPlanDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testPlanEntity = useAppSelector(state => state.testPlan.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testPlanDetailsHeading">Test Plan</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testPlanEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{testPlanEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{testPlanEntity.description}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{testPlanEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{testPlanEntity.createdAt ? <TextFormat value={testPlanEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Project</dt>
          <dd>{testPlanEntity.project ? testPlanEntity.project.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-plan" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-plan/${testPlanEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestPlanDetail;
