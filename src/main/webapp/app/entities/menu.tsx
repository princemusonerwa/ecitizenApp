import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/province">
        Province
      </MenuItem>
      <MenuItem icon="asterisk" to="/district">
        District
      </MenuItem>
      <MenuItem icon="asterisk" to="/sector">
        Sector
      </MenuItem>
      <MenuItem icon="asterisk" to="/cell">
        Cell
      </MenuItem>
      <MenuItem icon="asterisk" to="/village">
        Village
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-extended">
        User Extended
      </MenuItem>
      <MenuItem icon="asterisk" to="/office">
        Office
      </MenuItem>
      <MenuItem icon="asterisk" to="/umuturage">
        Umuturage
      </MenuItem>
      <MenuItem icon="asterisk" to="/complain">
        Complain
      </MenuItem>
      <MenuItem icon="asterisk" to="/category">
        Category
      </MenuItem>
      <MenuItem icon="asterisk" to="/attachment">
        Attachment
      </MenuItem>
      <MenuItem icon="asterisk" to="/organization">
        Organization
      </MenuItem>
      <MenuItem icon="asterisk" to="/umuyobozi">
        Umuyobozi
      </MenuItem>
      <MenuItem icon="asterisk" to="/umurimo">
        Umurimo
      </MenuItem>

      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
