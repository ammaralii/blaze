import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-case.reducer';

export const TestCaseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testCaseEntity = useAppSelector(state => state.testCase.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testCaseDetailsHeading">Test Case</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testCaseEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{testCaseEntity.title}</dd>
          <dt>
            <span id="estimate">Estimate</span>
          </dt>
          <dd>{testCaseEntity.estimate}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{testCaseEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{testCaseEntity.createdAt ? <TextFormat value={testCaseEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedBy">Updated By</span>
          </dt>
          <dd>{testCaseEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>{testCaseEntity.updatedAt ? <TextFormat value={testCaseEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="precondition">Precondition</span>
          </dt>
          <dd>{testCaseEntity.precondition}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{testCaseEntity.description}</dd>
          <dt>
            <span id="isAutomated">Is Automated</span>
          </dt>
          <dd>{testCaseEntity.isAutomated ? 'true' : 'false'}</dd>
          <dt>Test Suite</dt>
          <dd>{testCaseEntity.testSuite ? testCaseEntity.testSuite.id : ''}</dd>
          <dt>Section</dt>
          <dd>{testCaseEntity.section ? testCaseEntity.section.id : ''}</dd>
          <dt>Priority</dt>
          <dd>{testCaseEntity.priority ? testCaseEntity.priority.name : ''}</dd>
          <dt>Template</dt>
          <dd>{testCaseEntity.template ? testCaseEntity.template.id : ''}</dd>
          <dt>Milestone</dt>
          <dd>{testCaseEntity.milestone ? testCaseEntity.milestone.id : ''}</dd>
          <dt>Test Level</dt>
          <dd>
            {testCaseEntity.testLevels
              ? testCaseEntity.testLevels.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {testCaseEntity.testLevels && i === testCaseEntity.testLevels.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/test-case" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-case/${testCaseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestCaseDetail;
