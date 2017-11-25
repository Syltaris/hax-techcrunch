import { Template } from 'meteor/templating';
import { ReactiveVar } from 'meteor/reactive-var';

import './main.html';

Template.map.events({
  'click button'(e, instance) {
    e.preventDefault();

    Modal.show('GiftDetails');
  }
})

