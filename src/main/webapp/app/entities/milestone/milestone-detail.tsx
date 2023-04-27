import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './milestone.reducer';

export const MilestoneDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const milestoneEntity = useAppSelector(state => state.milestone.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="milestoneDetailsHeading">Milestone</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{milestoneEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{milestoneEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{milestoneEntity.description}</dd>
          <dt>
            <span id="reference">Reference</span>
          </dt>
          <dd>{milestoneEntity.reference}</dd>
          <dt>
            <span id="startDate">Start Date</span>
          </dt>
          <dd>
            {milestoneEntity.startDate ? <TextFormat value={milestoneEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">End Date</span>
          </dt>
          <dd>{milestoneEntity.endDate ? <TextFormat value={milestoneEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isCompleted">Is Completed</span>
          </dt>
          <dd>{milestoneEntity.isCompleted ? 'true' : 'false'}</dd>
          <dt>Parent Milestone</dt>
          <dd>{milestoneEntity.parentMilestone ? milestoneEntity.parentMilestone.id : ''}</dd>
          <dt>Project</dt>
          <dd>{milestoneEntity.project ? milestoneEntity.project.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/milestone" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/milestone/${milestoneEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MilestoneDetail;
