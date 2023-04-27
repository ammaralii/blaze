import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITemplateField } from 'app/shared/model/template-field.model';
import { getEntities as getTemplateFields } from 'app/entities/template-field/template-field.reducer';
import { ITestCase } from 'app/shared/model/test-case.model';
import { getEntities as getTestCases } from 'app/entities/test-case/test-case.reducer';
import { ITestCaseField } from 'app/shared/model/test-case-field.model';
import { getEntity, updateEntity, createEntity, reset } from './test-case-field.reducer';

export const TestCaseFieldUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const templateFields = useAppSelector(state => state.templateField.entities);
  const testCases = useAppSelector(state => state.testCase.entities);
  const testCaseFieldEntity = useAppSelector(state => state.testCaseField.entity);
  const loading = useAppSelector(state => state.testCaseField.loading);
  const updating = useAppSelector(state => state.testCaseField.updating);
  const updateSuccess = useAppSelector(state => state.testCaseField.updateSuccess);

  const handleClose = () => {
    navigate('/test-case-field' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTemplateFields({}));
    dispatch(getTestCases({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...testCaseFieldEntity,
      ...values,
      templateField: templateFields.find(it => it.id.toString() === values.templateField.toString()),
      testCase: testCases.find(it => it.id.toString() === values.testCase.toString()),
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
          ...testCaseFieldEntity,
          templateField: testCaseFieldEntity?.templateField?.id,
          testCase: testCaseFieldEntity?.testCase?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeApp.testCaseField.home.createOrEditLabel" data-cy="TestCaseFieldCreateUpdateHeading">
            Create or edit a Test Case Field
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
                <ValidatedField name="id" required readOnly id="test-case-field-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Expected Result"
                id="test-case-field-expectedResult"
                name="expectedResult"
                data-cy="expectedResult"
                type="text"
                validate={{
                  maxLength: { value: 65535, message: 'This field cannot be longer than 65535 characters.' },
                }}
              />
              <ValidatedField
                label="Value"
                id="test-case-field-value"
                name="value"
                data-cy="value"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                id="test-case-field-templateField"
                name="templateField"
                data-cy="templateField"
                label="Template Field"
                type="select"
                required
              >
                <option value="" key="0" />
                {templateFields
                  ? templateFields.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField id="test-case-field-testCase" name="testCase" data-cy="testCase" label="Test Case" type="select" required>
                <option value="" key="0" />
                {testCases
                  ? testCases.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-case-field" replace color="info">
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

export default TestCaseFieldUpdate;
