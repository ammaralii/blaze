import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITestCase } from 'app/shared/model/test-case.model';
import { getEntities as getTestCases } from 'app/entities/test-case/test-case.reducer';
import { ITestLevel } from 'app/shared/model/test-level.model';
import { getEntity, updateEntity, createEntity, reset } from './test-level.reducer';

export const TestLevelUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const testCases = useAppSelector(state => state.testCase.entities);
  const testLevelEntity = useAppSelector(state => state.testLevel.entity);
  const loading = useAppSelector(state => state.testLevel.loading);
  const updating = useAppSelector(state => state.testLevel.updating);
  const updateSuccess = useAppSelector(state => state.testLevel.updateSuccess);

  const handleClose = () => {
    navigate('/test-level' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTestCases({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...testLevelEntity,
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
          ...testLevelEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeApp.testLevel.home.createOrEditLabel" data-cy="TestLevelCreateUpdateHeading">
            Create or edit a Test Level
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="test-level-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="test-level-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-level" replace color="info">
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

export default TestLevelUpdate;
