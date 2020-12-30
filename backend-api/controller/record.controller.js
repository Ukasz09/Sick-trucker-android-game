const { response } = require("express");
const { records } = require("../models");
const db = require("../models");
const Records = db.records;

exports.findAll = (req, res) => {
  const { sort } = req.query;
  let sort_param = {};
  if (sort) sort_param = { time: sort };
  else sort_param = { createdAt: "desc" };

  Records.find({})
    .sort(sort_param)
    .then((data) => {
      res.send(data);
    })
    .catch((err) => {
      res.status(500).send({
        message: err.message || "Some error occurred while retrieving items. ",
      });
    });
};

exports.updateRecords = (req, res) => {
  if (Object.keys(req.body).length === 0) {
    res.status(400).send({ message: "Content cannot be empty!" });
    return;
  }

  const record = new Records({
    logoUrl: req.body.logoUrl,
    nick: req.body.nick,
    time: req.body.time,
  });

  console.log(record);

  Records.findOne({ nick: req.body.nick }, (_err, recordResp) => {
    if (recordResp) {
      const actualTime = recordResp.time;
      if (actualTime > record.time) {
        Records.findByIdAndUpdate(recordResp._id, req.body, { useFindAndModify: false })
          .then((data) => {
            if (!data) {
              res.status(404).send({
                message: `Cannot update record with nick=${record.nick}`,
              });
            } else res.send({ message: "Record was updated successfully." });
          })
          .catch((err) => {
            res.status(500).send({
              message: "Error updating User with nick: " + record.nick + ".Error:" + err,
            });
          });
      } else {
        res.status(200).send({ message: "Time not better than best" });
      }
    } else {
      record
        .save(record)
        .then((data) => {
          res.send(data);
        })
        .catch((err) => {
          res.status(500).send({
            message: err.message || "Some error occurred while creating the User.",
          });
        });
    }
  });
};
