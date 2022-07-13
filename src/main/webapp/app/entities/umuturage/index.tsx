import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Umuturage from './umuturage';
import UmuturageDetail from './umuturage-detail';
import UmuturageUpdate from './umuturage-update';
import UmuturageDeleteDialog from './umuturage-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UmuturageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UmuturageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UmuturageDetail} />
      <ErrorBoundaryRoute path={match.url} component={Umuturage} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UmuturageDeleteDialog} />
  </>
);

export default Routes;
