INSERT INTO `tfc_daw`.`examenes`
(`idExamen`,
`nivel`,
`nombre`,
`categoria`)
VALUES
(<{idExamen: }>,
<{nivel: }>,
<{nombre: }>,
<{categoria: }>);

INSERT INTO `tfc_daw`.`preguntas`
(`idPregunta`,
`Enunciado_Pregunta`,
`IdExamen`,
`categoria`)
VALUES
(<{idPregunta: }>,
<{Enunciado_Pregunta: }>,
<{IdExamen: }>,
<{categoria: }>);

INSERT INTO `tfc_daw`.`respuestas`
(`idRespuesta`,
`esCorrecta`,
`respuesta`,
`IdPregunta`)
VALUES
(<{idRespuesta: }>,
<{esCorrecta: }>,
<{respuesta: }>,
<{IdPregunta: }>);

INSERT INTO `tfc_daw`.`usuarios`
(`idUsuario`,
`Contraseña_Usuario`,
`CorreoElectronico`,
`Nombre_Usuario`,
`Tipo_Suscripcion`,
`Foto_Perfil`)
VALUES
(<{idUsuario: }>,
<{Contraseña_Usuario: }>,
<{CorreoElectronico: }>,
<{Nombre_Usuario: }>,
<{Tipo_Suscripcion: }>,
<{Foto_Perfil: }>);

INSERT INTO `tfc_daw`.`usuarios_examenes`
(`id`,
`fechaRealizacion`,
`nota`,
`idExamen`,
`idUsuario`)
VALUES
(<{id: }>,
<{fechaRealizacion: }>,
<{nota: }>,
<{idExamen: }>,
<{idUsuario: }>);