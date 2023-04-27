import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './section.reducer';

export const SectionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sectionEntity = useAppSelector(state => state.section.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sectionDetailsHeading">Section</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{sectionEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{sectionEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{sectionEntity.description}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{sectionEntity.createdAt ? <TextFormat value={sectionEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{sectionEntity.createdBy}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>{sectionEntity.updatedAt ? <TextFormat value={sectionEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedBy">Updated By</span>
          </dt>
          <dd>{sectionEntity.updatedBy}</dd>
          <dt>Test Suite</dt>
          <dd>{sectionEntity.testSuite ? sectionEntity.testSuite.id : ''}</dd>
          <dt>Parent Section</dt>
          <dd>{sectionEntity.parentSection ? sectionEntity.parentSection.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/section" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/section/${sectionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SectionDetail;
