import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Complain from './complain';
import ComplainDetail from './complain-detail';
import ComplainUpdate from './complain-update';
import ComplainDeleteDialog from './complain-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ComplainUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ComplainUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ComplainDetail} />
      <ErrorBoundaryRoute path={match.url} component={Complain} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ComplainDeleteDialog} />
  </>
);

export default Routes;
