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
import { useForm } from 'react-hook-form';

export const UmuturageUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const [searchInput, setSearchInput] = useState('');
  const [value, setValue] = useState({ nationalId: '', ubudehe: '', village: '', amazina: '', gender: '', phone: '' });

  const [formSection, setFormSection] = useState(0);

  const {
    watch,
    register,
    handleSubmit,
    formState: { errors, isValid },
  } = useForm({
    mode: 'all',
  });

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const villages = useAppSelector(state => state.village.entities);
  const umuturageEntity = useAppSelector(state => state.umuturage.entity);
  const loading = useAppSelector(state => state.umuturage.loading);
  const updating = useAppSelector(state => state.umuturage.updating);
  const updateSuccess = useAppSelector(state => state.umuturage.updateSuccess);

  const handleClose = () => {
    props.history.push('/umuturage' + props.location.search);
  };

  const backDisabled = () => {
    if (formSection === 0) {
      return true;
    } else if (formSection > 0) {
      return false;
    }
  };

  const completeSection = () => {
    setFormSection(curr => curr + 1);
  };

  const moveBackSection = () => {
    setFormSection(curr => curr - 1);
  };

  const handleButton = () => {
    if (formSection < 2) {
      return (
        <div>
          <button type="button" onClick={moveBackSection} disabled={backDisabled()} className="btn btn-success m-2">
            Back
          </button>
          <button type="button" onClick={completeSection} disabled={!isValid} className="btn btn-success m-2">
            Next
          </button>
        </div>
      );
    } else if (formSection === 2) {
      return (
        <div>
          <h1>Preview all the value </h1>
          <button type="button" onClick={moveBackSection} disabled={backDisabled()} className="btn btn-success m-2">
            Back
          </button>
        </div>
      );
    } else {
      return undefined;
    }
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
    console.log('You have reached here.');
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

  // const defaultValues = () =>
  //   isNew
  //     ? {
  //         dob: displayDefaultDateTime(),
  //       }
  //     : {
  //         gender: 'MALE',
  //         ...umuturageEntity,
  //         dob: convertDateTimeFromServer(umuturageEntity.dob),
  //         user: umuturageEntity?.user?.id,
  //         village: umuturageEntity?.village?.id,
  //       };

  const registerOptions = {
    indangamuntu: {
      required: 'This field is required',
      minLength: {
        value: 16,
        message: 'This field is required to be at least 16 characters.',
      },
      maxLength: {
        value: 16,
        message: 'Indangamuntu must have at least 16 characters',
      },
    },
    amazina: {
      required: 'This field is required',
      minLength: {
        value: 3,
        message: 'This field is required to be at least 3 characters.',
      },
    },
    dob: {
      required: 'This field is required',
    },
    ubudehe: {
      required: 'This field is required.',
      minLength: {
        value: 1,
        message: 'This field is required to be at least 1 characters.',
      },
      maxLength: {
        value: 1,
        message: 'This field cannot be longer than 1 characters.',
      },
    },
    phone: {
      minLength: {
        value: 13,
        message: 'This field is required to be at least 13 characters.',
      },
      maxLength: {
        value: 13,
        message: 'This field cannot be longer than 13 characters.',
      },
    },
    email: {
      pattern: {
        value: /^[^@\s]+@[^@\s]+\.[^@\s]+$/,
        message: "This field should follow pattern for '^[^@\\s]+@[^@\\s]+\\.[^@\\s]+..",
      },
    },
    gender: {
      required: 'This field is required',
      minLength: {
        value: 4,
        message: 'This field is required to be at least 4 characters.',
      },
      maxLength: {
        value: 6,
        message: 'This field cannot be longer than 6 characters.',
      },
    },
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
            <ValidatedForm onSubmit={handleSubmit(saveEntity)}>
              {!isNew ? <ValidatedField name="id" required readOnly id="umuturage-id" label="ID" validate={{ required: true }} /> : null}
              {formSection === 0 && (
                <section>
                  <Row>
                    <div className="form-group mt-2">
                      <Col md="8">
                        <label htmlFor="indangamuntu">Indangamuntu</label>
                        <input
                          id="umuturage-indangamuntu"
                          name="indangamuntu"
                          data-cy="indangamuntu"
                          type="text"
                          {...register('indangamuntu', registerOptions.indangamuntu)}
                          onChange={e => setSearchInput(e.target.value)}
                          className="form-control"
                        />
                      </Col>
                      <small className="text-danger mb-2">{errors?.indangamuntu && errors.indangamuntu.message}</small>
                    </div>
                    {isNew && (
                      <Row>
                        <Col md="4">
                          <button type="button" onClick={getPersonIdentity} disabled={!isValid} className="btn btn-primary">
                            Click here
                          </button>
                        </Col>
                      </Row>
                    )}
                  </Row>
                </section>
              )}
              {formSection === 1 && (
                <section>
                  <div className="form-group mt-2">
                    <label htmlFor="amazina">Amazina</label>
                    <input
                      id="umuturage-amazina"
                      name="amazina"
                      data-cy="amazina"
                      type="text"
                      value={value.amazina && value.amazina}
                      {...register('amazina', registerOptions.amazina)}
                      onChange={event => setValue({ ...value, amazina: event.target.value })}
                      className="form-control"
                    />
                    <small className="text-danger mb-2">{errors?.amazina && errors.amazina.message}</small>
                  </div>
                  <div className="form-group mt-2">
                    <label htmlFor="dob">Date of Birth</label>
                    <input
                      id="umuturage-dob"
                      name="dob"
                      data-cy="dob"
                      type="datetime-local"
                      placeholder="YYYY-MM-DD HH:mm"
                      value={displayDefaultDateTime()}
                      {...register('dob', registerOptions.dob)}
                      className="form-control"
                    />
                    <small className="text-danger mb-2">{errors?.dob && errors.indangamuntu.dob}</small>
                  </div>
                  <div className="form-group mt-2">
                    <label htmlFor="gender">Gender</label>
                    <input
                      id="umuturage-gender"
                      name="gender"
                      data-cy="gender"
                      type="text"
                      value={value.gender && value.gender.toUpperCase()}
                      {...register('gender', registerOptions.gender)}
                      onChange={event => setValue({ ...value, gender: event.target.value })}
                      className="form-control"
                    ></input>
                    <small className="text-danger mb-2">{errors?.gender && errors.gender.message}</small>
                  </div>

                  <div className="form-group mt-2">
                    <label htmlFor="ubudeheCategory">Ubudehe Category</label>
                    <input
                      id="umuturage-ubudeheCategory"
                      name="ubudeheCategory"
                      data-cy="ubudeheCategory"
                      type="text"
                      value={value.ubudehe && value.ubudehe}
                      onChange={event => setValue({ ...value, ubudehe: event.target.value })}
                      {...register('ubudeheCategory', registerOptions.ubudehe)}
                      className="form-control"
                    />
                    <small className="text-danger mb-2">{errors?.ubudehe && errors.ubudehe.message}</small>
                  </div>
                  <div className="form-group mt-2">
                    <label htmlFor="phone">Phone</label>
                    <input
                      id="umuturage-phone"
                      name="phone"
                      data-cy="phone"
                      type="text"
                      value={value.phone}
                      {...register('phone', registerOptions.phone)}
                      onChange={event => setValue({ ...value, phone: event.target.value })}
                      className="form-control"
                    />
                    <small className="text-danger mb-2">{errors?.phone && errors.phone.message}</small>
                  </div>
                  <div className="form-group mt-2">
                    <label htmlFor="email">Email</label>
                    <input
                      id="umuturage-email"
                      name="email"
                      data-cy="email"
                      type="email"
                      {...register('email', registerOptions.email)}
                      className="form-control"
                    />
                    <small className="text-danger mb-2">{errors?.email && errors.email.message}</small>
                  </div>
                  <div className="form-group mt-2">
                    <label htmlFor="village">Village</label>
                    <select id="umuturage-village" name="village" data-cy="village" {...register('village')} className="form-control">
                      <option value="" key="0" />
                      {villages
                        ? villages.map(otherEntity => (
                            <option value={otherEntity.id} key={otherEntity.id}>
                              {otherEntity.name}
                            </option>
                          ))
                        : null}
                    </select>
                  </div>
                </section>
              )}
              <Row>
                <Col md="4">{handleButton()}</Col>
              </Row>
              {formSection === 2 && (
                <div>
                  <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                    <FontAwesomeIcon icon="save" />
                    &nbsp; Save
                  </Button>
                </div>
              )}
            </ValidatedForm>
          )}
        </Col>
        {/* {JSON.stringify(watch(), null, 2)} */}
      </Row>
    </div>
  );
};

export default UmuturageUpdate;
