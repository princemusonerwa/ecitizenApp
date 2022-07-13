import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISector } from 'app/shared/model/sector.model';
import { getEntities as getSectors } from 'app/entities/sector/sector.reducer';
import { ICell } from 'app/shared/model/cell.model';
import { getEntity, updateEntity, createEntity, reset } from './cell.reducer';

export const CellUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const sectors = useAppSelector(state => state.sector.entities);
  const cellEntity = useAppSelector(state => state.cell.entity);
  const loading = useAppSelector(state => state.cell.loading);
  const updating = useAppSelector(state => state.cell.updating);
  const updateSuccess = useAppSelector(state => state.cell.updateSuccess);
  const handleClose = () => {
    props.history.push('/cell');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getSectors({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...cellEntity,
      ...values,
      sector: sectors.find(it => it.id.toString() === values.sector.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...cellEntity,
          sector: cellEntity?.sector?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecitizenApp.cell.home.createOrEditLabel" data-cy="CellCreateUpdateHeading">
            Create or edit a Cell
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="cell-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="cell-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 3, message: 'This field is required to be at least 3 characters.' },
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField id="cell-sector" name="sector" data-cy="sector" label="Sector" type="select">
                <option value="" key="0" />
                {sectors
                  ? sectors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cell" replace color="info">
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

export default CellUpdate;
