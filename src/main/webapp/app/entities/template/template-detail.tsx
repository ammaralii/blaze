import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './template.reducer';

export const TemplateDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const templateEntity = useAppSelector(state => state.template.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="templateDetailsHeading">Template</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{templateEntity.id}</dd>
          <dt>
            <span id="templateName">Template Name</span>
          </dt>
          <dd>{templateEntity.templateName}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{templateEntity.createdAt ? <TextFormat value={templateEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createdBy">Created By</span>
          </dt>
          <dd>{templateEntity.createdBy}</dd>
          <dt>Company</dt>
          <dd>{templateEntity.company ? templateEntity.company.id : ''}</dd>
          <dt>Template Field</dt>
          <dd>
            {templateEntity.templateFields
              ? templateEntity.templateFields.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {templateEntity.templateFields && i === templateEntity.templateFields.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/template" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/template/${templateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TemplateDetail;
