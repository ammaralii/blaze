import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICompany } from 'app/shared/model/company.model';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import { ITemplateFieldType } from 'app/shared/model/template-field-type.model';
import { getEntities as getTemplateFieldTypes } from 'app/entities/template-field-type/template-field-type.reducer';
import { ITemplate } from 'app/shared/model/template.model';
import { getEntities as getTemplates } from 'app/entities/template/template.reducer';
import { ITemplateField } from 'app/shared/model/template-field.model';
import { getEntity, updateEntity, createEntity, reset } from './template-field.reducer';

export const TemplateFieldUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const companies = useAppSelector(state => state.company.entities);
  const templateFieldTypes = useAppSelector(state => state.templateFieldType.entities);
  const templates = useAppSelector(state => state.template.entities);
  const templateFieldEntity = useAppSelector(state => state.templateField.entity);
  const loading = useAppSelector(state => state.templateField.loading);
  const updating = useAppSelector(state => state.templateField.updating);
  const updateSuccess = useAppSelector(state => state.templateField.updateSuccess);

  const handleClose = () => {
    navigate('/template-field' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCompanies({}));
    dispatch(getTemplateFieldTypes({}));
    dispatch(getTemplates({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...templateFieldEntity,
      ...values,
      company: companies.find(it => it.id.toString() === values.company.toString()),
      templateFieldType: templateFieldTypes.find(it => it.id.toString() === values.templateFieldType.toString()),
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
          ...templateFieldEntity,
          company: templateFieldEntity?.company?.id,
          templateFieldType: templateFieldEntity?.templateFieldType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeApp.templateField.home.createOrEditLabel" data-cy="TemplateFieldCreateUpdateHeading">
            Create or edit a Template Field
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
                <ValidatedField name="id" required readOnly id="template-field-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Field Name"
                id="template-field-fieldName"
                name="fieldName"
                data-cy="fieldName"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField id="template-field-company" name="company" data-cy="company" label="Company" type="select" required>
                <option value="" key="0" />
                {companies
                  ? companies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField
                id="template-field-templateFieldType"
                name="templateFieldType"
                data-cy="templateFieldType"
                label="Template Field Type"
                type="select"
                required
              >
                <option value="" key="0" />
                {templateFieldTypes
                  ? templateFieldTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.type}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/template-field" replace color="info">
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

export default TemplateFieldUpdate;
