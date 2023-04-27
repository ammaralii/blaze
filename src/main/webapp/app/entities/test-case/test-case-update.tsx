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
import { ISection } from 'app/shared/model/section.model';
import { getEntities as getSections } from 'app/entities/section/section.reducer';
import { ITestCasePriority } from 'app/shared/model/test-case-priority.model';
import { getEntities as getTestCasePriorities } from 'app/entities/test-case-priority/test-case-priority.reducer';
import { ITemplate } from 'app/shared/model/template.model';
import { getEntities as getTemplates } from 'app/entities/template/template.reducer';
import { IMilestone } from 'app/shared/model/milestone.model';
import { getEntities as getMilestones } from 'app/entities/milestone/milestone.reducer';
import { ITestLevel } from 'app/shared/model/test-level.model';
import { getEntities as getTestLevels } from 'app/entities/test-level/test-level.reducer';
import { ITestCase } from 'app/shared/model/test-case.model';
import { getEntity, updateEntity, createEntity, reset } from './test-case.reducer';

export const TestCaseUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testSuites = useAppSelector(state => state.testSuite.entities);
  const sections = useAppSelector(state => state.section.entities);
  const testCasePriorities = useAppSelector(state => state.testCasePriority.entities);
  const templates = useAppSelector(state => state.template.entities);
  const milestones = useAppSelector(state => state.milestone.entities);
  const testLevels = useAppSelector(state => state.testLevel.entities);
  const testCaseEntity = useAppSelector(state => state.testCase.entity);
  const loading = useAppSelector(state => state.testCase.loading);
  const updating = useAppSelector(state => state.testCase.updating);
  const updateSuccess = useAppSelector(state => state.testCase.updateSuccess);

  const handleClose = () => {
    navigate('/test-case' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestSuites({}));
    dispatch(getSections({}));
    dispatch(getTestCasePriorities({}));
    dispatch(getTemplates({}));
    dispatch(getMilestones({}));
    dispatch(getTestLevels({}));
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
      ...testCaseEntity,
      ...values,
      testLevels: mapIdList(values.testLevels),
      testSuite: testSuites.find(it => it.id.toString() === values.testSuite.toString()),
      section: sections.find(it => it.id.toString() === values.section.toString()),
      priority: testCasePriorities.find(it => it.id.toString() === values.priority.toString()),
      template: templates.find(it => it.id.toString() === values.template.toString()),
      milestone: milestones.find(it => it.id.toString() === values.milestone.toString()),
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
          ...testCaseEntity,
          createdAt: convertDateTimeFromServer(testCaseEntity.createdAt),
          updatedAt: convertDateTimeFromServer(testCaseEntity.updatedAt),
          testSuite: testCaseEntity?.testSuite?.id,
          section: testCaseEntity?.section?.id,
          priority: testCaseEntity?.priority?.id,
          template: testCaseEntity?.template?.id,
          milestone: testCaseEntity?.milestone?.id,
          testLevels: testCaseEntity?.testLevels?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeApp.testCase.home.createOrEditLabel" data-cy="TestCaseCreateUpdateHeading">
            Create or edit a Test Case
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="test-case-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Title"
                id="test-case-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Estimate"
                id="test-case-estimate"
                name="estimate"
                data-cy="estimate"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField label="Created By" id="test-case-createdBy" name="createdBy" data-cy="createdBy" type="text" />
              <ValidatedField
                label="Created At"
                id="test-case-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Updated By" id="test-case-updatedBy" name="updatedBy" data-cy="updatedBy" type="text" />
              <ValidatedField
                label="Updated At"
                id="test-case-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Precondition"
                id="test-case-precondition"
                name="precondition"
                data-cy="precondition"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Description"
                id="test-case-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 65535, message: 'This field cannot be longer than 65535 characters.' },
                }}
              />
              <ValidatedField
                label="Is Automated"
                id="test-case-isAutomated"
                name="isAutomated"
                data-cy="isAutomated"
                check
                type="checkbox"
              />
              <ValidatedField id="test-case-testSuite" name="testSuite" data-cy="testSuite" label="Test Suite" type="select">
                <option value="" key="0" />
                {testSuites
                  ? testSuites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="test-case-section" name="section" data-cy="section" label="Section" type="select">
                <option value="" key="0" />
                {sections
                  ? sections.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="test-case-priority" name="priority" data-cy="priority" label="Priority" type="select" required>
                <option value="" key="0" />
                {testCasePriorities
                  ? testCasePriorities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField id="test-case-template" name="template" data-cy="template" label="Template" type="select">
                <option value="" key="0" />
                {templates
                  ? templates.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="test-case-milestone" name="milestone" data-cy="milestone" label="Milestone" type="select">
                <option value="" key="0" />
                {milestones
                  ? milestones.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Test Level" id="test-case-testLevel" data-cy="testLevel" type="select" multiple name="testLevels">
                <option value="" key="0" />
                {testLevels
                  ? testLevels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-case" replace color="info">
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

export default TestCaseUpdate;
