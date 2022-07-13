import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './attachment.reducer';

export const AttachmentDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const attachmentEntity = useAppSelector(state => state.attachment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="attachmentDetailsHeading">Attachment</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{attachmentEntity.id}</dd>
          <dt>
            <span id="attachmentLink">Attachment Link</span>
          </dt>
          <dd>
            {attachmentEntity.attachmentLink ? (
              <div>
                {attachmentEntity.attachmentLinkContentType ? (
                  <a onClick={openFile(attachmentEntity.attachmentLinkContentType, attachmentEntity.attachmentLink)}>Open&nbsp;</a>
                ) : null}
                <span>
                  {attachmentEntity.attachmentLinkContentType}, {byteSize(attachmentEntity.attachmentLink)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="attachmentType">Attachment Type</span>
          </dt>
          <dd>{attachmentEntity.attachmentType}</dd>
          <dt>Complain</dt>
          <dd>{attachmentEntity.complain ? attachmentEntity.complain.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/attachment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/attachment/${attachmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AttachmentDetail;
