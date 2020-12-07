module.exports = (mongoose) => {
  const schema = mongoose.Schema({
    logoUrl: String,
    nick: String,
    travelledMeters: Number,
  });
  schema.method("toJSON", function () {
    const { __v, _id, ...object } = this.toObject();
    return object;
  });

  return mongoose.model("record", schema);
};
