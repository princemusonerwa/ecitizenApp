import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Umuyobozi from './umuyobozi';
import UmuyoboziDetail from './umuyobozi-detail';
import UmuyoboziUpdate from './umuyobozi-update';
import UmuyoboziDeleteDialog from './umuyobozi-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UmuyoboziUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UmuyoboziUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UmuyoboziDetail} />
      <ErrorBoundaryRoute path={match.url} component={Umuyobozi} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UmuyoboziDeleteDialog} />
  </>
);

export default Routes;
