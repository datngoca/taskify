export const mapOrder = (originalArray, orderArray, key) => {
  if (!originalArray || !orderArray || !key) return [];
  const orderArrayNumber = orderArray.map(Number);
  const clonedArray = [...originalArray];
  return clonedArray.sort((a, b) => {
    return orderArrayNumber.indexOf(a[key]) - orderArrayNumber.indexOf(b[key]);
  });
};