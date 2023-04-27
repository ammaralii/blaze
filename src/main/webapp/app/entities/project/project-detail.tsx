import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './project.reducer';

export const ProjectDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const projectEntity = useAppSelector(state => state.project.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="projectDetailsHeading">Project</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{projectEntity.id}</dd>
          <dt>
            <span id="projectName">Project Name</span>
          </dt>
          <dd>{projectEntity.projectName}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{projectEntity.description}</dd>
          <dt>
            <span id="isactive">Isactive</span>
          </dt>
          <dd>{projectEntity.isactive ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{projectEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{projectEntity.createdAt ? <TextFormat value={projectEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedBy">Updated By</span>
          </dt>
          <dd>{projectEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>{projectEntity.updatedAt ? <TextFormat value={projectEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Default Template</dt>
          <dd>{projectEntity.defaultTemplate ? projectEntity.defaultTemplate.id : ''}</dd>
          <dt>Company</dt>
          <dd>{projectEntity.company ? projectEntity.company.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/project" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/project/${projectEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProjectDetail;
