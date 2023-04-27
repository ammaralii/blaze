import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-case-field-attachment.reducer';

export const TestCaseFieldAttachmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testCaseFieldAttachmentEntity = useAppSelector(state => state.testCaseFieldAttachment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testCaseFieldAttachmentDetailsHeading">Test Case Field Attachment</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{testCaseFieldAttachmentEntity.id}</dd>
          <dt>
            <span id="url">Url</span>
          </dt>
          <dd>{testCaseFieldAttachmentEntity.url}</dd>
          <dt>Test Case Field</dt>
          <dd>{testCaseFieldAttachmentEntity.testCaseField ? testCaseFieldAttachmentEntity.testCaseField.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-case-field-attachment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-case-field-attachment/${testCaseFieldAttachmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestCaseFieldAttachmentDetail;
