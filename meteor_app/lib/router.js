
Router.configure({
	layoutTemplate: 'layout',
	loadingTemplate: 'loading',
});

Router.route('/', {
    name: 'bmap'
});

var requireLogin = function() {
    if (!Meteor.user()) {
        if (Meteor.loggingIn()) {
            this.render('loading');
        } else {
            this.render('accessDenied');
        }
    } else {
        this.next();
    }
};

Router.onBeforeAction(requireLogin);