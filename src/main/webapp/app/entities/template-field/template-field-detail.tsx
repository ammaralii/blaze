import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './template-field.reducer';

export const TemplateFieldDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const templateFieldEntity = useAppSelector(state => state.templateField.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="templateFieldDetailsHeading">Template Field</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{templateFieldEntity.id}</dd>
          <dt>
            <span id="fieldName">Field Name</span>
          </dt>
          <dd>{templateFieldEntity.fieldName}</dd>
          <dt>Company</dt>
          <dd>{templateFieldEntity.company ? templateFieldEntity.company.id : ''}</dd>
          <dt>Template Field Type</dt>
          <dd>{templateFieldEntity.templateFieldType ? templateFieldEntity.templateFieldType.type : ''}</dd>
        </dl>
        <Button tag={Link} to="/template-field" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/template-field/${templateFieldEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TemplateFieldDetail;
