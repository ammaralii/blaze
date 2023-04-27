import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestSuite } from 'app/shared/model/test-suite.model';
import { getEntities as getTestSuites } from 'app/entities/test-suite/test-suite.reducer';
import { getEntities as getSections } from 'app/entities/section/section.reducer';
import { ISection } from 'app/shared/model/section.model';
import { getEntity, updateEntity, createEntity, reset } from './section.reducer';

export const SectionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testSuites = useAppSelector(state => state.testSuite.entities);
  const sections = useAppSelector(state => state.section.entities);
  const sectionEntity = useAppSelector(state => state.section.entity);
  const loading = useAppSelector(state => state.section.loading);
  const updating = useAppSelector(state => state.section.updating);
  const updateSuccess = useAppSelector(state => state.section.updateSuccess);

  const handleClose = () => {
    navigate('/section' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestSuites({}));
    dispatch(getSections({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...sectionEntity,
      ...values,
      testSuite: testSuites.find(it => it.id.toString() === values.testSuite.toString()),
      parentSection: sections.find(it => it.id.toString() === values.parentSection.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...sectionEntity,
          createdAt: convertDateTimeFromServer(sectionEntity.createdAt),
          updatedAt: convertDateTimeFromServer(sectionEntity.updatedAt),
          testSuite: sectionEntity?.testSuite?.id,
          parentSection: sectionEntity?.parentSection?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeApp.section.home.createOrEditLabel" data-cy="SectionCreateUpdateHeading">
            Create or edit a Section
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="section-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="section-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Description"
                id="section-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 65535, message: 'This field cannot be longer than 65535 characters.' },
                }}
              />
              <ValidatedField
                label="Created At"
                id="section-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Created By" id="section-createdBy" name="createdBy" data-cy="createdBy" type="text" />
              <ValidatedField
                label="Updated At"
                id="section-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Updated By" id="section-updatedBy" name="updatedBy" data-cy="updatedBy" type="text" />
              <ValidatedField id="section-testSuite" name="testSuite" data-cy="testSuite" label="Test Suite" type="select">
                <option value="" key="0" />
                {testSuites
                  ? testSuites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="section-parentSection" name="parentSection" data-cy="parentSection" label="Parent Section" type="select">
                <option value="" key="0" />
                {sections
                  ? sections.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/section" replace color="info">
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

export default SectionUpdate;
