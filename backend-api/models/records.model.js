module.exports = (mongoose) => {
  const schema = mongoose.Schema({
    logoUrl: String,
    nick: String,
    time: Number,
  });
  schema.method("toJSON", function () {
    const { __v, _id, ...object } = this.toObject();
    return object;
  });
  const Record = mongoose.model("record", schema);
  return Record;
};
