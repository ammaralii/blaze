import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITemplateFieldType } from 'app/shared/model/template-field-type.model';
import { getEntity, updateEntity, createEntity, reset } from './template-field-type.reducer';

export const TemplateFieldTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const templateFieldTypeEntity = useAppSelector(state => state.templateFieldType.entity);
  const loading = useAppSelector(state => state.templateFieldType.loading);
  const updating = useAppSelector(state => state.templateFieldType.updating);
  const updateSuccess = useAppSelector(state => state.templateFieldType.updateSuccess);

  const handleClose = () => {
    navigate('/template-field-type' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...templateFieldTypeEntity,
      ...values,
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
          ...templateFieldTypeEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeApp.templateFieldType.home.createOrEditLabel" data-cy="TemplateFieldTypeCreateUpdateHeading">
            Create or edit a Template Field Type
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
                <ValidatedField name="id" required readOnly id="template-field-type-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Type"
                id="template-field-type-type"
                name="type"
                data-cy="type"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField label="Is List" id="template-field-type-isList" name="isList" data-cy="isList" check type="checkbox" />
              <ValidatedField
                label="Attachments"
                id="template-field-type-attachments"
                name="attachments"
                data-cy="attachments"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/template-field-type" replace color="info">
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

export default TemplateFieldTypeUpdate;
