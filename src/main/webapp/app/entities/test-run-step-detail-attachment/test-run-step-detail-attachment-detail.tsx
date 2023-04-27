import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-run-step-detail-attachment.reducer';

export const TestRunStepDetailAttachmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testRunStepDetailAttachmentEntity = useAppSelector(state => state.testRunStepDetailAttachment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testRunStepDetailAttachmentDetailsHeading">Test Run Step Detail Attachment</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testRunStepDetailAttachmentEntity.id}</dd>
          <dt>
            <span id="url">Url</span>
          </dt>
          <dd>{testRunStepDetailAttachmentEntity.url}</dd>
          <dt>Test Run Step Detail</dt>
          <dd>{testRunStepDetailAttachmentEntity.testRunStepDetail ? testRunStepDetailAttachmentEntity.testRunStepDetail.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-run-step-detail-attachment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-run-step-detail-attachment/${testRunStepDetailAttachmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestRunStepDetailAttachmentDetail;
