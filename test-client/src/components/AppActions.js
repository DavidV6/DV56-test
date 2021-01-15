
const AppActions = function construct(backendHost) {
  this.backendHost = backendHost;
};

AppActions.prototype.init = function init() {
  return fetch(`http://${this.backendHost}/application/init`)
    .then(response => response.json());
};

AppActions.prototype.fetchCategories = function fetchCategories() {
  return fetch(`http://${this.backendHost}/categories`)
    .then(response => response.json());
};

AppActions.prototype.fetchVenues = function fetchVenues() {
  return fetch(`http://${this.backendHost}/venues`)
    .then(response => response.json());
};

AppActions.prototype.fetchEvents = function fetchEvents() {
  return fetch(`http://${this.backendHost}/events`)
    .then(response => response.json());
};

AppActions.prototype.createEvent = function createEvent(event) {
  return fetch(`http://${this.backendHost}/events/upsert`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(event),
  })
    .then(response => response.json());
};

AppActions.prototype.createDate = function createDate(date) {
  return fetch(`http://${this.backendHost}/dates/upsert`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(date),
  })
    .then(response => response.json());
};

AppActions.prototype.upsertCategory = function upsertCategory(category) {
  return fetch(`http://${this.backendHost}/categories/upsert`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(category),
  })
    .then(response => response.json());
};

export default AppActions;
