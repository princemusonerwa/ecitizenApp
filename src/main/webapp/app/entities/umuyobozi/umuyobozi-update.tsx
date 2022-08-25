import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { OfficeType } from 'app/shared/model/enumerations/office-type.model';

import { IUmurimo } from 'app/shared/model/umurimo.model';
import { getEntities as getUmurimos } from 'app/entities/umurimo/umurimo.reducer';
import { IOffice } from 'app/shared/model/office.model';
import { getEntities as getOffices } from 'app/entities/office/office.reducer';
import { IUmuyobozi } from 'app/shared/model/umuyobozi.model';
import { getEntity, updateEntity, createEntity, reset, getEntities } from './umuyobozi.reducer';

import axios from 'axios';

export const UmuyoboziUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const umurimos = useAppSelector(state => state.umurimo.entities);
  const offices = useAppSelector(state => state.office.entities);
  const umuyoboziEntity = useAppSelector(state => state.umuyobozi.entity);
  const loading = useAppSelector(state => state.umuyobozi.loading);
  const updating = useAppSelector(state => state.umuyobozi.updating);
  const updateSuccess = useAppSelector(state => state.umuyobozi.updateSuccess);
  const officeTypeValues = Object.keys(OfficeType);

  const [selected, setSelected] = useState('');

  const handleClose = () => {
    props.history.push('/umuyobozi' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUmurimos({}));
    dispatch(getOffices({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...umuyoboziEntity,
      ...values,
      umurimo: umurimos.find(it => it.id.toString() === values.umurimo.toString()),
      office: offices.find(it => it.id.toString() === values.office.toString()),
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
          ...umuyoboziEntity,
          umurimo: umuyoboziEntity?.umurimo?.id,
          office: umuyoboziEntity?.office?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecitizenApp.umuyobozi.home.createOrEditLabel" data-cy="UmuyoboziCreateUpdateHeading">
            Create or edit a Umuyobozi
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="umuyobozi-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="First Name"
                id="umuyobozi-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 3, message: 'This field is required to be at least 3 characters.' },
                  maxLength: { value: 100, message: 'This field cannot be longer than 100 characters.' },
                }}
              />
              <ValidatedField
                label="Last Name"
                id="umuyobozi-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 3, message: 'This field is required to be at least 3 characters.' },
                  maxLength: { value: 100, message: 'This field cannot be longer than 100 characters.' },
                }}
              />
              <ValidatedField
                label="Phone One"
                id="umuyobozi-phoneOne"
                name="phoneOne"
                data-cy="phoneOne"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 13, message: 'This field is required to be at least 13 characters starting with +250.' },
                  maxLength: { value: 13, message: 'This field cannot be longer than 13 characters starting with +250.' },
                }}
              />
              <ValidatedField
                label="Phone Two"
                id="umuyobozi-phoneTwo"
                name="phoneTwo"
                data-cy="phoneTwo"
                type="text"
                validate={{
                  minLength: {
                    value: 13,
                    message: 'This field is not required, if you provide the input, it must have 13 characters starting with +250.',
                  },
                  maxLength: {
                    value: 13,
                    message: 'This field is not required, if you provide the input, it must have 13 characters starting with +250.',
                  },
                }}
              />
              <ValidatedField
                label="Email"
                id="umuyobozi-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  pattern: {
                    value: /^[^@\s]+@[^@\s]+\.[^@\s]+$/,
                    message: "This field should follow pattern for '^[^@\\s]+@[^@\\s]+\\.[^@\\s]+..",
                  },
                }}
              />
              <div className="form-group mt-2 mb-2">
                <label htmlFor="level">Umuyobozi Level</label>
                <select
                  id="umuyobozi-office_select"
                  name="level"
                  className="form-select"
                  value={selected}
                  onChange={e => setSelected(e.target.value)}
                >
                  <option value="" key="0" />
                  {officeTypeValues.map(officeType => (
                    <option value={officeType} key={officeType}>
                      {officeType}
                    </option>
                  ))}
                </select>
              </div>
              <ValidatedField
                id="umuyobozi-office"
                name="office"
                data-cy="office"
                label="Area"
                type="select"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              >
                <option value="" key="0" />
                {offices
                  ? offices
                      .filter(otherEntity => otherEntity.officeType === selected)
                      .map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                  : null}
              </ValidatedField>
              <ValidatedField id="umuyobozi-umurimo" name="umurimo" data-cy="umurimo" label="Umurimo" type="select">
                <option value="" key="0" />
                {umurimos
                  ? umurimos
                      .filter(otherEntity => otherEntity.officeType === selected)
                      .map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.umurimo}
                        </option>
                      ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/umuyobozi" replace color="info">
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

export default UmuyoboziUpdate;
