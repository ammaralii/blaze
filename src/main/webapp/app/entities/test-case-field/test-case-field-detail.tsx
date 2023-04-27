import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-case-field.reducer';

export const TestCaseFieldDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testCaseFieldEntity = useAppSelector(state => state.testCaseField.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testCaseFieldDetailsHeading">Test Case Field</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testCaseFieldEntity.id}</dd>
          <dt>
            <span id="expectedResult">Expected Result</span>
          </dt>
          <dd>{testCaseFieldEntity.expectedResult}</dd>
          <dt>
            <span id="value">Value</span>
          </dt>
          <dd>{testCaseFieldEntity.value}</dd>
          <dt>Template Field</dt>
          <dd>{testCaseFieldEntity.templateField ? testCaseFieldEntity.templateField.id : ''}</dd>
          <dt>Test Case</dt>
          <dd>{testCaseFieldEntity.testCase ? testCaseFieldEntity.testCase.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-case-field" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-case-field/${testCaseFieldEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestCaseFieldDetail;
