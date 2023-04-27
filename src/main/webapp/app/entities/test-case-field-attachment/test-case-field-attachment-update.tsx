import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestCaseField } from 'app/shared/model/test-case-field.model';
import { getEntities as getTestCaseFields } from 'app/entities/test-case-field/test-case-field.reducer';
import { ITestCaseFieldAttachment } from 'app/shared/model/test-case-field-attachment.model';
import { getEntity, updateEntity, createEntity, reset } from './test-case-field-attachment.reducer';

export const TestCaseFieldAttachmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testCaseFields = useAppSelector(state => state.testCaseField.entities);
  const testCaseFieldAttachmentEntity = useAppSelector(state => state.testCaseFieldAttachment.entity);
  const loading = useAppSelector(state => state.testCaseFieldAttachment.loading);
  const updating = useAppSelector(state => state.testCaseFieldAttachment.updating);
  const updateSuccess = useAppSelector(state => state.testCaseFieldAttachment.updateSuccess);

  const handleClose = () => {
    navigate('/test-case-field-attachment' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestCaseFields({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...testCaseFieldAttachmentEntity,
      ...values,
      testCaseField: testCaseFields.find(it => it.id.toString() === values.testCaseField.toString()),
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
          ...testCaseFieldAttachmentEntity,
          testCaseField: testCaseFieldAttachmentEntity?.testCaseField?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeApp.testCaseFieldAttachment.home.createOrEditLabel" data-cy="TestCaseFieldAttachmentCreateUpdateHeading">
            Create or edit a Test Case Field Attachment
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
                <ValidatedField name="id" required readOnly id="test-case-field-attachment-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Url"
                id="test-case-field-attachment-url"
                name="url"
                data-cy="url"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  maxLength: { value: 65535, message: 'This field cannot be longer than 65535 characters.' },
                }}
              />
              <ValidatedField
                id="test-case-field-attachment-testCaseField"
                name="testCaseField"
                data-cy="testCaseField"
                label="Test Case Field"
                type="select"
                required
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
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-case-field-attachment" replace color="info">
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

export default TestCaseFieldAttachmentUpdate;
