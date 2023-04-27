import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestRunDetails } from 'app/shared/model/test-run-details.model';
import { getEntities as getTestRunDetails } from 'app/entities/test-run-details/test-run-details.reducer';
import { ITestCaseField } from 'app/shared/model/test-case-field.model';
import { getEntities as getTestCaseFields } from 'app/entities/test-case-field/test-case-field.reducer';
import { ITestStatus } from 'app/shared/model/test-status.model';
import { getEntities as getTestStatuses } from 'app/entities/test-status/test-status.reducer';
import { ITestRunStepDetails } from 'app/shared/model/test-run-step-details.model';
import { getEntity, updateEntity, createEntity, reset } from './test-run-step-details.reducer';

export const TestRunStepDetailsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testRunDetails = useAppSelector(state => state.testRunDetails.entities);
  const testCaseFields = useAppSelector(state => state.testCaseField.entities);
  const testStatuses = useAppSelector(state => state.testStatus.entities);
  const testRunStepDetailsEntity = useAppSelector(state => state.testRunStepDetails.entity);
  const loading = useAppSelector(state => state.testRunStepDetails.loading);
  const updating = useAppSelector(state => state.testRunStepDetails.updating);
  const updateSuccess = useAppSelector(state => state.testRunStepDetails.updateSuccess);

  const handleClose = () => {
    navigate('/test-run-step-details' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestRunDetails({}));
    dispatch(getTestCaseFields({}));
    dispatch(getTestStatuses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...testRunStepDetailsEntity,
      ...values,
      testRunDetail: testRunDetails.find(it => it.id.toString() === values.testRunDetail.toString()),
      stepDetail: testCaseFields.find(it => it.id.toString() === values.stepDetail.toString()),
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
          ...testRunStepDetailsEntity,
          testRunDetail: testRunStepDetailsEntity?.testRunDetail?.id,
          stepDetail: testRunStepDetailsEntity?.stepDetail?.id,
          status: testRunStepDetailsEntity?.status?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeApp.testRunStepDetails.home.createOrEditLabel" data-cy="TestRunStepDetailsCreateUpdateHeading">
            Create or edit a Test Run Step Details
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
                <ValidatedField name="id" required readOnly id="test-run-step-details-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Actual Result"
                id="test-run-step-details-actualResult"
                name="actualResult"
                data-cy="actualResult"
                type="text"
                validate={{
                  maxLength: { value: 65535, message: 'This field cannot be longer than 65535 characters.' },
                }}
              />
              <ValidatedField
                id="test-run-step-details-testRunDetail"
                name="testRunDetail"
                data-cy="testRunDetail"
                label="Test Run Detail"
                type="select"
              >
                <option value="" key="0" />
                {testRunDetails
                  ? testRunDetails.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="test-run-step-details-stepDetail"
                name="stepDetail"
                data-cy="stepDetail"
                label="Step Detail"
                type="select"
              >
                <option value="" key="0" />
                {testCaseFields
                  ? testCaseFields.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="test-run-step-details-status" name="status" data-cy="status" label="Status" type="select">
                <option value="" key="0" />
                {testStatuses
                  ? testStatuses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-run-step-details" replace color="info">
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

export default TestRunStepDetailsUpdate;
