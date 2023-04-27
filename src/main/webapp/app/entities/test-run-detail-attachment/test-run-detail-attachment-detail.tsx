import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-run-detail-attachment.reducer';

export const TestRunDetailAttachmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testRunDetailAttachmentEntity = useAppSelector(state => state.testRunDetailAttachment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testRunDetailAttachmentDetailsHeading">Test Run Detail Attachment</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testRunDetailAttachmentEntity.id}</dd>
          <dt>
            <span id="url">Url</span>
          </dt>
          <dd>{testRunDetailAttachmentEntity.url}</dd>
          <dt>Test Run Detail</dt>
          <dd>{testRunDetailAttachmentEntity.testRunDetail ? testRunDetailAttachmentEntity.testRunDetail.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-run-detail-attachment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-run-detail-attachment/${testRunDetailAttachmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestRunDetailAttachmentDetail;
