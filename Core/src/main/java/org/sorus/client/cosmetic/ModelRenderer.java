package org.sorus.client.cosmetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sorus.client.Sorus;
import org.sorus.client.util.Axis;
import org.sorus.client.version.IGLHelper;

public class ModelRenderer {

  private final Model model;
  private final List<ModelCube> cubes = new ArrayList<>();
  private final Map<String, RotationPoint> rotationPoints = new HashMap<>();

  public ModelRenderer(Model model) {
    this.model = model;
  }

  public void addCube(
      int xTextureOffset,
      int yTextureOffset,
      float x,
      float y,
      float z,
      float width,
      float height,
      float depth,
      double scale) {
    cubes.add(
        new ModelCube(this, xTextureOffset, yTextureOffset, x, y, z, width, height, depth, scale));
  }

  public void addRotationPoint(String name, double x, double y, double z) {
    this.rotationPoints.put(name, new RotationPoint(x, y, z));
  }

  public RotationPoint getRotationPoint(String name) {
    return this.rotationPoints.get(name);
  }

  public void render() {
    IGLHelper glHelper = Sorus.getSorus().getVersion().getData(IGLHelper.class);
    glHelper.push();
    for (RotationPoint rotationPoint : rotationPoints.values()) {
      glHelper.translate(rotationPoint.getX(), rotationPoint.getY(), rotationPoint.getZ());
      glHelper.rotate(Axis.X, rotationPoint.getXRot());
      glHelper.rotate(Axis.Y, rotationPoint.getYRot());
      glHelper.rotate(Axis.Z, rotationPoint.getZRot());
      glHelper.translate(-rotationPoint.getX(), -rotationPoint.getY(), -rotationPoint.getZ());
    }
    for (ModelCube cube : cubes) {
      cube.render();
    }
    glHelper.pop();
  }

  public Model getModel() {
    return model;
  }
}
