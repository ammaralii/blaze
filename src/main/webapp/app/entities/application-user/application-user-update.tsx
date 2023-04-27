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
import { IProject } from 'app/shared/model/project.model';
import { getEntities as getProjects } from 'app/entities/project/project.reducer';
import { IRole } from 'app/shared/model/role.model';
import { getEntities as getRoles } from 'app/entities/role/role.reducer';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { getEntity, updateEntity, createEntity, reset } from './application-user.reducer';

export const ApplicationUserUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const companies = useAppSelector(state => state.company.entities);
  const projects = useAppSelector(state => state.project.entities);
  const roles = useAppSelector(state => state.role.entities);
  const applicationUserEntity = useAppSelector(state => state.applicationUser.entity);
  const loading = useAppSelector(state => state.applicationUser.loading);
  const updating = useAppSelector(state => state.applicationUser.updating);
  const updateSuccess = useAppSelector(state => state.applicationUser.updateSuccess);

  const handleClose = () => {
    navigate('/application-user' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCompanies({}));
    dispatch(getProjects({}));
    dispatch(getRoles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.lastActive = convertDateTimeToServer(values.lastActive);
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...applicationUserEntity,
      ...values,
      projects: mapIdList(values.projects),
      roles: mapIdList(values.roles),
      company: companies.find(it => it.id.toString() === values.company.toString()),
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
          lastActive: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...applicationUserEntity,
          lastActive: convertDateTimeFromServer(applicationUserEntity.lastActive),
          createdAt: convertDateTimeFromServer(applicationUserEntity.createdAt),
          updatedAt: convertDateTimeFromServer(applicationUserEntity.updatedAt),
          company: applicationUserEntity?.company?.id,
          projects: applicationUserEntity?.projects?.map(e => e.id.toString()),
          roles: applicationUserEntity?.roles?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="blazeApp.applicationUser.home.createOrEditLabel" data-cy="ApplicationUserCreateUpdateHeading">
            Create or edit a Application User
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
                <ValidatedField name="id" required readOnly id="application-user-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="First Name"
                id="application-user-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Last Name"
                id="application-user-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Password"
                id="application-user-password"
                name="password"
                data-cy="password"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Last Active"
                id="application-user-lastActive"
                name="lastActive"
                data-cy="lastActive"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Status"
                id="application-user-status"
                name="status"
                data-cy="status"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField label="Created By" id="application-user-createdBy" name="createdBy" data-cy="createdBy" type="text" />
              <ValidatedField
                label="Created At"
                id="application-user-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Updated By" id="application-user-updatedBy" name="updatedBy" data-cy="updatedBy" type="text" />
              <ValidatedField
                label="Updated At"
                id="application-user-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="User Email"
                id="application-user-userEmail"
                name="userEmail"
                data-cy="userEmail"
                type="text"
                validate={{
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Is Deleted"
                id="application-user-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField id="application-user-company" name="company" data-cy="company" label="Company" type="select">
                <option value="" key="0" />
                {companies
                  ? companies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Project" id="application-user-project" data-cy="project" type="select" multiple name="projects">
                <option value="" key="0" />
                {projects
                  ? projects.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Role" id="application-user-role" data-cy="role" type="select" multiple name="roles">
                <option value="" key="0" />
                {roles
                  ? roles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/application-user" replace color="info">
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

export default ApplicationUserUpdate;
