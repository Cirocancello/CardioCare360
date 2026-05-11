import api from "./api";

export const getPazienteById = async (id) => {
  const response = await api.get(`/paziente/${id}`);
  return response.data;
};
