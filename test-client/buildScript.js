const path = require('path');
var shell = require('shelljs');
const { exec } = require('child_process');

const startDB = function () {
  console.log('Starting database... ');
  // const dbScript = path.dirname("start_db").split(path.sep).pop();
  const paths = __dirname.split(path.sep);

  var dbScript = '';
  for (let index = 0; index < paths.length - 1; index++) {
    const element = paths[index];
    dbScript = dbScript + element + '/';
  }

  shell.exec('docker rm $(docker stop $(docker ps -f ancestor=postgres:9.6-alpine ))')

  exec(dbScript + 'start_db.sh', (err, stdout, stderr) => {
    if (err) {
      console.log(err)
      return;
    }
    console.log('Database started!');
  });

  return;
}

/**
 * Start Database service
 */
startDB();