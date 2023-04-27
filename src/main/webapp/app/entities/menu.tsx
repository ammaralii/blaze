import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/application-user">
        Application User
      </MenuItem>
      <MenuItem icon="asterisk" to="/company">
        Company
      </MenuItem>
      <MenuItem icon="asterisk" to="/milestone">
        Milestone
      </MenuItem>
      <MenuItem icon="asterisk" to="/permission">
        Permission
      </MenuItem>
      <MenuItem icon="asterisk" to="/project">
        Project
      </MenuItem>
      <MenuItem icon="asterisk" to="/role">
        Role
      </MenuItem>
      <MenuItem icon="asterisk" to="/section">
        Section
      </MenuItem>
      <MenuItem icon="asterisk" to="/template">
        Template
      </MenuItem>
      <MenuItem icon="asterisk" to="/template-field">
        Template Field
      </MenuItem>
      <MenuItem icon="asterisk" to="/template-field-type">
        Template Field Type
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-case">
        Test Case
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-case-attachment">
        Test Case Attachment
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-case-field">
        Test Case Field
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-case-field-attachment">
        Test Case Field Attachment
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-case-priority">
        Test Case Priority
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-level">
        Test Level
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-plan">
        Test Plan
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-run">
        Test Run
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-run-detail-attachment">
        Test Run Detail Attachment
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-run-details">
        Test Run Details
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-run-step-detail-attachment">
        Test Run Step Detail Attachment
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-run-step-details">
        Test Run Step Details
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-status">
        Test Status
      </MenuItem>
      <MenuItem icon="asterisk" to="/test-suite">
        Test Suite
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
