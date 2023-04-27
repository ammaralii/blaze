import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestLevel } from 'app/shared/model/test-level.model';
import { getEntities as getTestLevels } from 'app/entities/test-level/test-level.reducer';
import { IMilestone } from 'app/shared/model/milestone.model';
import { getEntities as getMilestones } from 'app/entities/milestone/milestone.reducer';
import { ITestRun } from 'app/shared/model/test-run.model';
import { getEntity, updateEntity, createEntity, reset } from './test-run.reducer';

export const TestRunUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testLevels = useAppSelector(state => state.testLevel.entities);
  const milestones = useAppSelector(state => state.milestone.entities);
  const testRunEntity = useAppSelector(state => state.testRun.entity);
  const loading = useAppSelector(state => state.testRun.loading);
  const updating = useAppSelector(state => state.testRun.updating);
  const updateSuccess = useAppSelector(state => state.testRun.updateSuccess);

  const handleClose = () => {
    navigate('/test-run' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestLevels({}));
    dispatch(getMilestones({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...testRunEntity,
      ...values,
      testLevel: testLevels.find(it => it.id.toString() === values.testLevel.toString()),
      mileStone: milestones.find(it => it.id.toString() === values.mileStone.toString()),
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
        }
      : {
          ...testRunEntity,
          createdAt: convertDateTimeFromServer(testRunEntity.createdAt),
          testLevel: testRunEntity?.testLevel?.id,
          mileStone: testRunEntity?.mileStone?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeApp.testRun.home.createOrEditLabel" data-cy="TestRunCreateUpdateHeading">
            Create or edit a Test Run
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="test-run-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="test-run-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Description"
                id="test-run-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Created At"
                id="test-run-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Created By" id="test-run-createdBy" name="createdBy" data-cy="createdBy" type="text" />
              <ValidatedField id="test-run-testLevel" name="testLevel" data-cy="testLevel" label="Test Level" type="select">
                <option value="" key="0" />
                {testLevels
                  ? testLevels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="test-run-mileStone" name="mileStone" data-cy="mileStone" label="Mile Stone" type="select">
                <option value="" key="0" />
                {milestones
                  ? milestones.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-run" replace color="info">
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

export default TestRunUpdate;
