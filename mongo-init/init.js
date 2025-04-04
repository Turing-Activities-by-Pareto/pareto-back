db.getSiblingDB('activities').createUser({
    user: 'bisco',
    pwd: 'biscobisco',
    roles: [ { role: 'readWrite', db: 'activities' } ]
});