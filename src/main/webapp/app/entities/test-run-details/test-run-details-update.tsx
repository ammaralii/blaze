import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestRun } from 'app/shared/model/test-run.model';
import { getEntities as getTestRuns } from 'app/entities/test-run/test-run.reducer';
import { ITestCase } from 'app/shared/model/test-case.model';
import { getEntities as getTestCases } from 'app/entities/test-case/test-case.reducer';
import { ITestStatus } from 'app/shared/model/test-status.model';
import { getEntities as getTestStatuses } from 'app/entities/test-status/test-status.reducer';
import { ITestRunDetails } from 'app/shared/model/test-run-details.model';
import { getEntity, updateEntity, createEntity, reset } from './test-run-details.reducer';

export const TestRunDetailsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testRuns = useAppSelector(state => state.testRun.entities);
  const testCases = useAppSelector(state => state.testCase.entities);
  const testStatuses = useAppSelector(state => state.testStatus.entities);
  const testRunDetailsEntity = useAppSelector(state => state.testRunDetails.entity);
  const loading = useAppSelector(state => state.testRunDetails.loading);
  const updating = useAppSelector(state => state.testRunDetails.updating);
  const updateSuccess = useAppSelector(state => state.testRunDetails.updateSuccess);

  const handleClose = () => {
    navigate('/test-run-details' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestRuns({}));
    dispatch(getTestCases({}));
    dispatch(getTestStatuses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...testRunDetailsEntity,
      ...values,
      testRun: testRuns.find(it => it.id.toString() === values.testRun.toString()),
      testCase: testCases.find(it => it.id.toString() === values.testCase.toString()),
      status: testStatuses.find(it => it.id.toString() === values.status.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...testRunDetailsEntity,
          testRun: testRunDetailsEntity?.testRun?.id,
          testCase: testRunDetailsEntity?.testCase?.id,
          status: testRunDetailsEntity?.status?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeApp.testRunDetails.home.createOrEditLabel" data-cy="TestRunDetailsCreateUpdateHeading">
            Create or edit a Test Run Details
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="test-run-details-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Result Detail"
                id="test-run-details-resultDetail"
                name="resultDetail"
                data-cy="resultDetail"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Jira Id"
                id="test-run-details-jiraId"
                name="jiraId"
                data-cy="jiraId"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField label="Created By" id="test-run-details-createdBy" name="createdBy" data-cy="createdBy" type="text" />
              <ValidatedField label="Executed By" id="test-run-details-executedBy" name="executedBy" data-cy="executedBy" type="text" />
              <ValidatedField id="test-run-details-testRun" name="testRun" data-cy="testRun" label="Test Run" type="select">
                <option value="" key="0" />
                {testRuns
                  ? testRuns.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="test-run-details-testCase" name="testCase" data-cy="testCase" label="Test Case" type="select">
                <option value="" key="0" />
                {testCases
                  ? testCases.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="test-run-details-status" name="status" data-cy="status" label="Status" type="select">
                <option value="" key="0" />
                {testStatuses
                  ? testStatuses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-run-details" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TestRunDetailsUpdate;
