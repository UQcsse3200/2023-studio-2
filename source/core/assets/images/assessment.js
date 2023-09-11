// Example for reading and parsing a CSV file using 'csv-parser'
const fs = require('fs');
const csv = require('csv-parser');

const data = [];

fs.createReadStream('your-file.csv')
  .pipe(csv())
  .on('data', (row) => {
    // Process each row of data and store it in your data structure
    data.push(row);
  })
  .on('end', () => {
    // All data has been read and parsed
    console.log(data);
  });
