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
import { ITestRunDetailAttachment } from 'app/shared/model/test-run-detail-attachment.model';
import { getEntity, updateEntity, createEntity, reset } from './test-run-detail-attachment.reducer';

export const TestRunDetailAttachmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testRunDetails = useAppSelector(state => state.testRunDetails.entities);
  const testRunDetailAttachmentEntity = useAppSelector(state => state.testRunDetailAttachment.entity);
  const loading = useAppSelector(state => state.testRunDetailAttachment.loading);
  const updating = useAppSelector(state => state.testRunDetailAttachment.updating);
  const updateSuccess = useAppSelector(state => state.testRunDetailAttachment.updateSuccess);

  const handleClose = () => {
    navigate('/test-run-detail-attachment' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestRunDetails({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...testRunDetailAttachmentEntity,
      ...values,
      testRunDetail: testRunDetails.find(it => it.id.toString() === values.testRunDetail.toString()),
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
          ...testRunDetailAttachmentEntity,
          testRunDetail: testRunDetailAttachmentEntity?.testRunDetail?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeApp.testRunDetailAttachment.home.createOrEditLabel" data-cy="TestRunDetailAttachmentCreateUpdateHeading">
            Create or edit a Test Run Detail Attachment
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
                <ValidatedField name="id" required readOnly id="test-run-detail-attachment-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Url"
                id="test-run-detail-attachment-url"
                name="url"
                data-cy="url"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  maxLength: { value: 65535, message: 'This field cannot be longer than 65535 characters.' },
                }}
              />
              <ValidatedField
                id="test-run-detail-attachment-testRunDetail"
                name="testRunDetail"
                data-cy="testRunDetail"
                label="Test Run Detail"
                type="select"
                required
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
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-run-detail-attachment" replace color="info">
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

export default TestRunDetailAttachmentUpdate;
