
Template.bmap.helpers({
  get_markers() {
    return GiftMarkers.find({});
  }
})

Template.bmap.events({
    'click button'(e, instance) {
      e.preventDefault();
  
      Modal.show('GiftDetails');
    }
  })
  
  
  