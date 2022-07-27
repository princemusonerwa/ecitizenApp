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
import { getEntity, updateEntity, apiUrl, createEntity, reset } from './umuturage.reducer';
import axios from 'axios';

export const UmuturageUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const [searchInput, setSearchInput] = useState('');
  const [value, setValue] = useState({ nationalId: '', ubudehe: '', village: '', amazina: '', gender: '', phone: '' });

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

  // Get Umuturage By Id

  const getPerson = async (id: string | number) => {
    const { data } = await axios.get(`${apiUrl}/indentification/${id}`);
    const personIdentity = data.households[0];
    const familyMember = personIdentity.members.persons;
    const p = familyMember.find(person => person.nationalId === searchInput);
    console.log(p);
    const pId = {
      nationalId: p.nationalId,
      ubudehe: personIdentity.category.label,
      village: personIdentity.location.village.code,
      amazina: p.firstName + ' ' + p.lastName,
      gender: p.gender.label,
      phone: p.phone,
    };
    setValue({
      nationalId: pId.nationalId,
      ubudehe: pId.ubudehe,
      village: pId.village,
      amazina: pId.amazina,
      gender: pId.gender,
      phone: pId.phone,
    });
  };

  const getPersonIdentity = () => {
    getPerson(searchInput);
  };

  const saveEntity = values => {
    values.dob = convertDateTimeToServer(values.dob);
    values.indangamuntu = value.nationalId ? value.nationalId : '';

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

  const handleInput = event => {
    if (isNew) {
      setSearchInput(event.target.value);
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
              <Row>
                <Col md="8">
                  <ValidatedField
                    label="Indangamuntu"
                    id="umuturage-indangamuntu"
                    name="indangamuntu"
                    data-cy="indangamuntu"
                    type="text"
                    onChange={handleInput}
                    validate={{
                      required: { value: true, message: 'This field is required.' },
                      minLength: { value: 16, message: 'This field is required to be at least 16 characters.' },
                      maxLength: { value: 16, message: 'This field cannot be longer than 16 characters.' },
                    }}
                  />
                </Col>
                {isNew ? (
                  <Col md="4">
                    <button onClick={getPersonIdentity} className="btn btn-primary">
                      Click here
                    </button>
                  </Col>
                ) : (
                  ''
                )}
              </Row>
              <ValidatedField
                label="Amazina"
                id="umuturage-amazina"
                name="amazina"
                data-cy="amazina"
                type="text"
                value={value.amazina ? value.amazina : ''}
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
              <ValidatedField
                label="Gender"
                id="umuturage-gender"
                name="gender"
                data-cy="gender"
                type="text"
                value={value.gender ? value.gender.toUpperCase() : ''}
              ></ValidatedField>
              <ValidatedField
                label="Ubudehe Category"
                id="umuturage-ubudeheCategory"
                name="ubudeheCategory"
                data-cy="ubudeheCategory"
                type="text"
                value={value.ubudehe ? value.ubudehe : ''}
                onChange={event => setValue({ ...value, ubudehe: event.target.value })}
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
                value={value.phone}
                onChange={event => setValue({ ...value, phone: event.target.value })}
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
