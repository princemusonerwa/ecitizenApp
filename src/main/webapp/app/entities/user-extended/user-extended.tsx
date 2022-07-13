import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserExtended } from 'app/shared/model/user-extended.model';
import { getEntities } from './user-extended.reducer';

export const UserExtended = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const userExtendedList = useAppSelector(state => state.userExtended.entities);
  const loading = useAppSelector(state => state.userExtended.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="user-extended-heading" data-cy="UserExtendedHeading">
        User Extendeds
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/user-extended/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new User Extended
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userExtendedList && userExtendedList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Phone</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userExtendedList.map((userExtended, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-extended/${userExtended.id}`} color="link" size="sm">
                      {userExtended.id}
                    </Button>
                  </td>
                  <td>{userExtended.phone}</td>
                  <td>{userExtended.user ? userExtended.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-extended/${userExtended.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/user-extended/${userExtended.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-extended/${userExtended.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No User Extendeds found</div>
        )}
      </div>
    </div>
  );
};

export default UserExtended;
