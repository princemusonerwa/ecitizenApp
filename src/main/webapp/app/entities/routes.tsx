import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Province from './province';
import District from './district';
import Sector from './sector';
import Cell from './cell';
import Village from './village';
import UserExtended from './user-extended';
import Office from './office';
import Umuturage from './umuturage';
import Complain from './complain';
import Category from './category';
import Attachment from './attachment';
import Organization from './organization';
import Umuyobozi from './umuyobozi';
import Umurimo from './umurimo';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}province`} component={Province} />
        <ErrorBoundaryRoute path={`${match.url}district`} component={District} />
        <ErrorBoundaryRoute path={`${match.url}sector`} component={Sector} />
        <ErrorBoundaryRoute path={`${match.url}cell`} component={Cell} />
        <ErrorBoundaryRoute path={`${match.url}village`} component={Village} />
        <ErrorBoundaryRoute path={`${match.url}user-extended`} component={UserExtended} />
        <ErrorBoundaryRoute path={`${match.url}office`} component={Office} />
        <ErrorBoundaryRoute path={`${match.url}umuturage`} component={Umuturage} />
        <ErrorBoundaryRoute path={`${match.url}complain`} component={Complain} />
        <ErrorBoundaryRoute path={`${match.url}category`} component={Category} />
        <ErrorBoundaryRoute path={`${match.url}attachment`} component={Attachment} />
        <ErrorBoundaryRoute path={`${match.url}organization`} component={Organization} />
        <ErrorBoundaryRoute path={`${match.url}umuyobozi`} component={Umuyobozi} />
        <ErrorBoundaryRoute path={`${match.url}umurimo`} component={Umurimo} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
