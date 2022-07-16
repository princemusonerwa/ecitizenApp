import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IVillage } from 'app/shared/model/village.model';
import { getEntities as getVillages } from 'app/entities/village/village.reducer';
import { IUmuturage } from 'app/shared/model/umuturage.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { getEntity, updateEntity, createEntity, reset } from './umuturage.reducer';

export const UmuturageUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const villages = useAppSelector(state => state.village.entities);
  const umuturageEntity = useAppSelector(state => state.umuturage.entity);
  const loading = useAppSelector(state => state.umuturage.loading);
  const updating = useAppSelector(state => state.umuturage.updating);
  const updateSuccess = useAppSelector(state => state.umuturage.updateSuccess);
  const genderValues = Object.keys(Gender);
  const handleClose = () => {
    props.history.push('/umuturage' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
    dispatch(getVillages({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dob = convertDateTimeToServer(values.dob);

    const entity = {
      ...umuturageEntity,
      ...values,
      village: villages.find(it => it.id.toString() === values.village.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dob: displayDefaultDateTime(),
        }
      : {
          gender: 'MALE',
          ...umuturageEntity,
          dob: convertDateTimeFromServer(umuturageEntity.dob),
          user: umuturageEntity?.user?.id,
          village: umuturageEntity?.village?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecitizenApp.umuturage.home.createOrEditLabel" data-cy="UmuturageCreateUpdateHeading">
            Create or edit a Umuturage
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="umuturage-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Indangamuntu"
                id="umuturage-indangamuntu"
                name="indangamuntu"
                data-cy="indangamuntu"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 16, message: 'This field is required to be at least 16 characters.' },
                  maxLength: { value: 16, message: 'This field cannot be longer than 16 characters.' },
                }}
              />
              <ValidatedField
                label="Amazina"
                id="umuturage-amazina"
                name="amazina"
                data-cy="amazina"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 3, message: 'This field is required to be at least 3 characters.' },
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Dob"
                id="umuturage-dob"
                name="dob"
                data-cy="dob"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Gender" id="umuturage-gender" name="gender" data-cy="gender" type="select">
                {genderValues.map(gender => (
                  <option value={gender} key={gender}>
                    {gender}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Ubudehe Category"
                id="umuturage-ubudeheCategory"
                name="ubudeheCategory"
                data-cy="ubudeheCategory"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 1, message: 'This field is required to be at least 1 characters.' },
                  maxLength: { value: 1, message: 'This field cannot be longer than 1 characters.' },
                }}
              />
              <ValidatedField
                label="Phone"
                id="umuturage-phone"
                name="phone"
                data-cy="phone"
                type="text"
                validate={{
                  minLength: { value: 13, message: 'This field is required to be at least 13 characters.' },
                  maxLength: { value: 13, message: 'This field cannot be longer than 13 characters.' },
                }}
              />
              <ValidatedField
                label="Email"
                id="umuturage-email"
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
              <ValidatedField id="umuturage-village" name="village" data-cy="village" label="Village" type="select">
                <option value="" key="0" />
                {villages
                  ? villages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/umuturage" replace color="info">
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

export default UmuturageUpdate;
